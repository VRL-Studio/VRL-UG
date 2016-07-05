/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2016 Goethe Universität Frankfurt am Main, Germany
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UGException extends Exception {

    protected ArrayList<MsgItem> msgItems = new ArrayList<MsgItem>();
    static protected boolean showDetailedException = false;

    public static boolean isShowDetailedException() {
        return showDetailedException;
    }

    public static void setShowDetailedException(boolean showDetailedException) {
        UGException.showDetailedException = showDetailedException;
    }

    protected static class MsgItem {

        public String msg = null;
        public String file = null;
        public String line = null;

        public MsgItem(String msg, String file, String line) {
            this.msg = msg;
            this.file = file;
            this.line = line;
        }
    }

    public UGException(String message) {
        super(message);

        int numMsg = parseNumMessages(message);

        for (int i = 0; i < numMsg; ++i) {
            msgItems.add(parseMsgItem(message, i));
        }
    }

    @Override
    public String getMessage() {
        String msg = "<div><pre>\n";

        if (showDetailedException) {
            for (int i = 0; i < msgItems.size(); ++i) {
                msg += msgItems.get(i).msg + "\n";
                msg += "  at File: " + msgItems.get(i).file + "\n";
                msg += "  at Line: " + msgItems.get(i).line + "\n";
                msg += "\n---- caused by ----\n";
            }
        } else {
            if (msgItems.size() > 0) {
                msg += msgItems.get(0).msg + "\n";
            }
        }
        msg += "</div></pre>";
        return msg;
    }

    public static MsgItem parseMsgItem(String message, int i) {

        String msg = extractStringBetweenTags("Msg", message, i);
        String file = extractStringBetweenTags("File", message, i);
        String line = extractStringBetweenTags("Line", message, i);

        return new MsgItem(msg, file, line);
    }

    public static String extractStringBetweenTags(String tag, String msg, int i) {
        Pattern p = Pattern.compile(
                "<!--Start" + tag + ":" + i + ":-->"
                + "(.*)"
                + "<!--End" + tag + ":" + i + ":-->",
                Pattern.DOTALL);

        Matcher matcher = p.matcher(msg);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static int parseNumMessages(String message) {
        Pattern linePattern = Pattern.compile("<!--NumMsg:\\d+:-->");
        Matcher matcher = linePattern.matcher(message);

        int line = 0;
        if (matcher.find()) {
            String lineString = matcher.group();
            Pattern numberPattern = Pattern.compile("\\d+");
            matcher = numberPattern.matcher(lineString);
            if (matcher.find()) {
                line = new Integer(matcher.group());
            }
        }
        return line;
    }
}
