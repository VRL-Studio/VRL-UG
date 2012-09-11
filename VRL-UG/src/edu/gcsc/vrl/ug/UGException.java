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
