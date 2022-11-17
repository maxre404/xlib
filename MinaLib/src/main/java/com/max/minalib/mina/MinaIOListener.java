package com.max.minalib.mina;

import org.apache.mina.core.session.IoSession;

public interface MinaIOListener {

    void onConnect(Object session);

    void onReConnect(Object session);


    void onmessageReceived(Object session, Object message);

}
