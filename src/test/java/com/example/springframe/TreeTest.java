package com.example.springframe;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.springframe.entity.SysPermission;
import com.example.springframe.license.License;
import com.example.springframe.license.LicenseCreator;
import com.example.springframe.utils.tree.TreeUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TreeTest {
    @Test
    void generateTree() {
        String str1="{\n" +
                "      \"id\": 2,\n" +
                "      \"code\": \"e54d063f-2132-43d3-aaf7-cdb0508a737c\",\n" +
                "      \"type\": 0,\n" +
                "      \"name\": \"草稿箱\",\n" +
                "      \"description\": \"平台草稿箱菜单\",\n" +
                "      \"pid\": 0,\n" +
                "      \"menuType\": 0,\n" +
                "      \"keystr\": \"platform_case_list\",\n" +
                "      \"routing\": \"/drafts\",\n" +
                "      \"sort\": -1,\n" +
                "      \"children\": []\n" +
                "    }";
        String str2="{\n" +
                "          \"id\": 28,\n" +
                "          \"code\": \"97ebbdb6-9640-419a-91ea-e87e9208443b\",\n" +
                "          \"type\": 0,\n" +
                "          \"name\": \"查看\",\n" +
                "          \"description\": \"平台草稿箱详情按钮\",\n" +
                "          \"pid\": 2,\n" +
                "          \"menuType\": 1,\n" +
                "          \"keystr\": \"platform_case_details\",\n" +
                "          \"routing\": \"/drafts/detail\",\n" +
                "          \"sort\": 0,\n" +
                "          \"children\": []\n" +
                "        }";
        String str3="{\n" +
                "          \"id\": 29,\n" +
                "          \"code\": \"8f9c6496-a4da-4312-a247-0cd3f45f45dc\",\n" +
                "          \"type\": 0,\n" +
                "          \"name\": \"新增\",\n" +
                "          \"description\": \"平台草稿箱创建按钮\",\n" +
                "          \"pid\": 28,\n" +
                "          \"menuType\": 1,\n" +
                "          \"keystr\": \"platform_case_update\",\n" +
                "          \"routing\": null,\n" +
                "          \"sort\": 2,\n" +
                "          \"children\": []\n" +
                "        }";
        String str4=" {\n" +
                "          \"id\": 30,\n" +
                "          \"code\": \"901afad3-2464-4ad6-b2af-84e9c3e6ed04\",\n" +
                "          \"type\": 0,\n" +
                "          \"name\": \"修改\",\n" +
                "          \"description\": \"平台草稿箱编辑按钮\",\n" +
                "          \"pid\": 28,\n" +
                "          \"menuType\": 1,\n" +
                "          \"keystr\": \"platform_case_update\",\n" +
                "          \"routing\": \"/drafts/edit\",\n" +
                "          \"sort\": 4,\n" +
                "          \"children\": []\n" +
                "        }";
        String str5="{\n" +
                "          \"id\": 31,\n" +
                "          \"code\": \"db704d1b-14d3-42cb-a53d-e4894a6b63ad\",\n" +
                "          \"type\": 0,\n" +
                "          \"name\": \"删除\",\n" +
                "          \"description\": \"平台草稿箱删除按钮\",\n" +
                "          \"pid\": 30,\n" +
                "          \"menuType\": 1,\n" +
                "          \"keystr\": \"platform_case_delete\",\n" +
                "          \"routing\": null,\n" +
                "          \"sort\": 3,\n" +
                "          \"children\": []\n" +
                "        }";
        String str6=" {\n" +
                "          \"id\": 32,\n" +
                "          \"code\": \"901afad3-2464-4ad6-b2af-84e9c3e6ed04\",\n" +
                "          \"type\": 0,\n" +
                "          \"name\": \"修改1\",\n" +
                "          \"description\": \"平台草稿箱编辑按钮\",\n" +
                "          \"pid\": 28,\n" +
                "          \"menuType\": 1,\n" +
                "          \"keystr\": \"platform_case_update\",\n" +
                "          \"routing\": \"/drafts/edit\",\n" +
                "          \"sort\": 1,\n" +
                "          \"children\": []\n" +
                "        }";
        String str7=" {\n" +
                "          \"id\": 33,\n" +
                "          \"code\": \"901afad3-2464-4ad6-b2af-84e9c3e6ed04\",\n" +
                "          \"type\": 0,\n" +
                "          \"name\": \"修改2\",\n" +
                "          \"description\": \"平台草稿箱编辑按钮\",\n" +
                "          \"pid\": 28,\n" +
                "          \"menuType\": 1,\n" +
                "          \"keystr\": \"platform_case_update\",\n" +
                "          \"routing\": \"/drafts/edit\",\n" +
                "          \"sort\": 3,\n" +
                "          \"children\": []\n" +
                "        }";
        String str8=" {\n" +
                "          \"id\": 34,\n" +
                "          \"code\": \"901afad3-2464-4ad6-b2af-84e9c3e6ed04\",\n" +
                "          \"type\": 0,\n" +
                "          \"name\": \"修改3\",\n" +
                "          \"description\": \"平台草稿箱编辑按钮\",\n" +
                "          \"pid\": 28,\n" +
                "          \"menuType\": 1,\n" +
                "          \"keystr\": \"platform_case_update\",\n" +
                "          \"routing\": \"/drafts/edit\",\n" +
                "          \"sort\": 5,\n" +
                "          \"children\": []\n" +
                "        }";
        List<SysPermission> list =new ArrayList<>();
        list.add(JSONUtil.toBean(str1, SysPermission.class));
        list.add(JSONUtil.toBean(str2, SysPermission.class));
        list.add(JSONUtil.toBean(str3, SysPermission.class));
        list.add(JSONUtil.toBean(str4, SysPermission.class));
        list.add(JSONUtil.toBean(str5, SysPermission.class));
        list.add(JSONUtil.toBean(str6, SysPermission.class));
        list.add(JSONUtil.toBean(str7, SysPermission.class));
        list.add(JSONUtil.toBean(str8, SysPermission.class));
        JSONArray objects = TreeUtils.genListTreeByExtend(list);
        System.out.println(objects);
    }
}
