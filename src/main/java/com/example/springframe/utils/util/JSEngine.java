package com.example.springframe.utils.util;

import com.example.springframe.exception.InfinityException;
import com.example.springframe.exception.NaNException;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 获取javascript执行引擎，执行 js 操作
 *
 * @author 谢长春 on 2017/10/31 .
 */
@Slf4j
public final class JSEngine {
    private final ScriptEngine engine;
    private static volatile JSEngine instance;

    private JSEngine() {
//        js, rhino, JavaScript, javascript, ECMAScript, ecmascript
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    public static JSEngine getInstance() {
        if (null == instance) {
            synchronized (JSEngine.class) {
                if (null == instance) {
                    instance = new JSEngine();
                }
            }
        }
        return instance;
    }

    public ScriptEngine getEngine() {
        return engine;
    }

    /**
     * 获取数学算式计算值，执行异常将会抛出 ScriptException <br>
     * 例：(1+1)*2+Math.abs(-1)<br>
     *
     * @param formula String 算术表达式
     * @return String
     * @throws ScriptException 执行异常
     */
    public Num compute(final String formula) throws ScriptException {
        if (Util.isEmpty(formula)) {
            throw new NaNException(String.format("%s=NaN", formula));
        }
        final String value = eval(formula);
        if (Objects.equals("NaN", value)) {
            throw new NaNException(String.format("%s=%s", formula, value));
        } else if (Objects.equals("Infinity", value)) {
            throw new InfinityException(String.format("%s=%s", formula, value));
        }
        return Num.of(value);
    }

    /**
     * 执行 JS - eval()函数，执行异常将会抛出 ScriptException
     * 例：1<2?'正确':'不正确'
     *
     * @param script String js代码字符串
     * @param values Object[] script 中可以用 <%%> 进行占位，values的值将会替换占位符，替换规则为 <%%> 索引顺序
     * @return String
     * @throws ScriptException 执行异常
     */
    public String eval(String script, Object... values) throws ScriptException {
        Objects.requireNonNull(script, "参数【script】是必须的");
        if (Objects.nonNull(values)) {
            for (Object value : values) {
                script = script.replaceFirst("<%[\\da-zA-Z]*%>", value.toString());
            }
        }
        return engine.eval(script).toString();
    }

    /**
     * 执行 JS - eval()函数，执行异常将会抛出 ScriptException
     * 例：1<2?'正确':'不正确'
     *
     * @param script String js代码字符串，script 中可以用 <%key%>
     * @param map    Map<String, Object> map中的元素将会替换占位符，替换规则为 <%entry.getKey()%> = entry.getValue()
     * @return String
     * @throws ScriptException 执行异常
     */
    public String eval(String script, Map<String, Object> map) throws ScriptException {
        Objects.requireNonNull(script, "参数【script】是必须的");
        if (Objects.nonNull(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                script = script.replaceFirst(MessageFormat.format("<%{0}%>", entry.getKey()), entry.getValue().toString());
            }
        }
        return engine.eval(script).toString();
    }

    public static void main(String[] args) {
        try {
            System.out.println(JSEngine.getInstance().getEngine().eval("12*10"));
            System.out.println(JSEngine.getInstance().compute("(1+3)/2"));
            System.out.println(JSEngine.getInstance().compute("(1+3)*5").format());
            System.out.println(JSEngine.getInstance().eval("(18620000.00)+(-8760000.00)-(0)-(-360000.00)"));
            System.out.println(JSEngine.getInstance().eval("(function (o) {var count = o.贬损者 + o.被动者 + o.推荐者;return count === 0 ? 0 : parseInt((o.推荐者 / count) * 100 - (o.贬损者 / count) * 100);})({贬损者:<%%>,被动者:<%%>, 推荐者:<%%>})", 176, 608, 40));
            System.out.println(JSEngine.getInstance().eval("(function (o) {var count = o.贬损者 + o.被动者 + o.推荐者;return count === 0 ? 0 : parseInt((o.推荐者 / count) * 100 - (o.贬损者 / count) * 100);})({贬损者:<%贬损者%>,被动者:<%被动者%>, 推荐者:<%推荐者%>})", new HashMap<String, Object>() {{
                put("贬损者", 176);
                put("被动者", 608);
                put("推荐者", 40);
            }}));

        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
