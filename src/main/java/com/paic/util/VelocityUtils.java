package com.paic.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import cn.hutool.core.util.ObjectUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * @author zyh
 * @Description:
 * @date 2022/9/2211:16
 */
public final class VelocityUtils {

    private VelocityUtils(){
    }

    public static String process(String content, Map<String, Object> dataModel) throws IOException {
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        VelocityContext context = new VelocityContext(dataModel);
        try (StringWriter writer = new StringWriter()) {
            ve.evaluate(context, writer, "", content);
            return writer.toString();
        }
    }

    public static String parse(String filePath, Map<String, Object> dataModel) {

        String result = "";
        if (ObjectUtil.isEmpty(filePath)) {
            return result;
        }
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        try (StringWriter sw = new StringWriter()) {
            Template template = ve.getTemplate(filePath);
            VelocityContext ctx = new VelocityContext(dataModel);
            template.merge(ctx, sw);
            result = sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}