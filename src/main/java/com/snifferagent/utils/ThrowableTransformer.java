/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package com.snifferagent.utils;

/**
 *  异常格式转换类
 *  @author :chao.cheng
 */
public enum ThrowableTransformer {
    /**
     * 模拟单例类
     */
    INSTANCE;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public String convert2String(Throwable throwable, final int maxLength) {
        final StringBuilder stackMessage = new StringBuilder();
        Throwable causeException = throwable;
        while (causeException != null) {
            stackMessage.append(printExceptionInfo(causeException));

            boolean overMaxLength = printStackElement(throwable.getStackTrace(), new AppendListener() {

                @Override
                public void append(String value) {
                    stackMessage.append(value);
                }

                @Override
                public boolean overMaxLength() {
                    return stackMessage.length() > maxLength;
                }
            });

            if (overMaxLength) {
                break;
            }

            causeException = throwable.getCause();
        }

        return stackMessage.toString();
    }

    private String printExceptionInfo(Throwable causeException) {
        return causeException.toString() + LINE_SEPARATOR;
    }

    private boolean printStackElement(StackTraceElement[] stackTrace, AppendListener printListener) {
        for (StackTraceElement traceElement : stackTrace) {
            printListener.append("at " + traceElement + LINE_SEPARATOR);
            if (printListener.overMaxLength()) {
                return true;
            }
        }
        return false;
    }

    private interface AppendListener {
        void append(String value);

        boolean overMaxLength();
    }
}
