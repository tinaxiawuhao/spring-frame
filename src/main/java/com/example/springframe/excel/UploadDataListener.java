package com.example.springframe.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springframe.exception.BusinessException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板的读取类
 *
 */
// 有个很重要的点 UploadDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class UploadDataListener extends AnalysisEventListener<UploadDataInterface> {
    /**
     * 每隔100条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    private List<Object> cachedDataList = new ArrayList<>(BATCH_COUNT);
    /**
     * 这个是一个service。当然如果不用存储这个对象没用。
     */
    private IService iService;
    private Class uploadDataClass;


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param uploadDAO
     */
    public UploadDataListener(IService uploadDAO) {
        this.iService = uploadDAO;
    }

    public UploadDataListener(IService uploadDAO,Class uploadDataClass) {
        this.iService = uploadDAO;
        this.uploadDataClass = uploadDataClass;
    }

    //easyexcel每读取一行数据都会回调一次该方法处理数据
    @SneakyThrows
    @Override
    public void invoke(UploadDataInterface t, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSON(t));
        Object data = uploadDataClass.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(t,data);
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList.clear();
        }
    }

    /**
     * 读取额外信息,在invoke方法执行完成后,doAfterAllAnalysed方法前执行
     * 需要增加重写extra方法
     * @param extra
     * @param context
     */
    @Override
    public void extra(CellExtra extra, AnalysisContext context) {

    }


    //excel文档解析完毕后回调一次
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //可以将data数据解析批量插入到数据库
        System.out.println("excel文档解析完毕...");
        //判断如果data中仍有没存到数据库的，最后保存一次
        if(cachedDataList.size()>0){
            saveData();
            log.info("所有数据解析完成！");
            cachedDataList.clear();
        }
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        iService.saveBatch(cachedDataList);
        log.info("存储数据库成功！");
    }

    public static <T,U> T cast(Class<T> clazz,U obj) {
        if(clazz.isInstance(obj)){
            return (T) obj;
        }
        throw new BusinessException("类型转换错误!");
    }
}
