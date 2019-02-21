package common;

/*
 *  消息抽象类
 */
public class message {

    //空消息
    public static final int EMPTY_MESSAGE = 0;
    //客户端到服务器私聊消息
    public static final int SINGLECHAT_MESSAGE = 1;
    //客户端到服务器群聊消息
    public static final int CHATS_MESSAGE = 2;
    //服务器到客户端刷新列表消息
    public static final int FLUSH_MESSAGE = 3;
    //客户端下线消息
    public static final int CLIENT_LINE_OFF = 4;
    //客户端上线消息
    public static final int CLIENT_LINE_ON = 5;


}
