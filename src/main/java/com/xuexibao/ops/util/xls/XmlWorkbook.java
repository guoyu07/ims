/*
* $Id$ Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.xuexibao.ops.util.xls;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流式xml excel生成器, 暂未实现缓冲刷新
 *
 * @author: chenhonglei  Date: 12-11-27 Time: 下午9:50
 */
public class XmlWorkbook {

    private static final Logger log = LoggerFactory.getLogger(XmlWorkbook.class);

    public static final int DEFAULT_WINDOW_SIZE = 100;

    private Writer writer;

    public XmlWorkbook(OutputStream out) {
        this.writer = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8));
    }

    public void write(String content) {
        try {
            writer.write(content, 0, content.length());
            flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
