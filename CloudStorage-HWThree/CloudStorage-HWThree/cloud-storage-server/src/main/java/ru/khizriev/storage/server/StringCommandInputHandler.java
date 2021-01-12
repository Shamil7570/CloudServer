package ru.khizriev.storage.server;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;
import ru.khizriev.storage.server.exception.UserLoginException;
import ru.khizriev.storage.server.exception.UserPassException;
import ru.khizriev.storage.server.helper.AuthInfo;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StringCommandInputHandler extends SimpleChannelInboundHandler<String> {

    private UserDAO userDAO;
//    private static final List<Channel> channels = new ArrayList<>();
    private Path rootPath;


    public StringCommandInputHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        if (s.startsWith("/auth ")) {
            try {
                AuthInfo authInfo = new AuthInfo(s);

                if (userDAO.getUser(authInfo.getName(), authInfo.getPass())) {
                    System.out.println(s);
                    rootPath = Paths.get(".").resolve(authInfo.getName());
//                    channels.add(channelHandlerContext.channel());
                    channelHandlerContext.channel().writeAndFlush("/AuthOK " + rootPath.normalize());
                }
            } catch (UserLoginException | UserPassException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
