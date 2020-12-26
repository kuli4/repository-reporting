package pro.kuli4.repository.common.helpers;

public class StringBuildHelper {
    public static final int messageIdSize = 20;

    public static String buildMessageId(String owner, long id) {
        StringBuilder sb = new StringBuilder(owner + "_");
        String idStr = String.valueOf(id);
        sb.append("0".repeat(Math.max(0, messageIdSize - (owner.length() + 1 + idStr.length()))));
        sb.append(idStr);
        return sb.toString();
    }

    public static String buildCorrelationId(String repcode, String year, String messageId) {
        return "[" + repcode + "]-[" + year + "]-[" + messageId + "]";
    }

    public static String buildPartyId(String type, String value) {
        return type + "_" + value;
    }
}
