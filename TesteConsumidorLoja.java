package com.adi.middleware.middleware.jms.a2;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteConsumidorLoja {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection("guest","senha");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination fila = (Destination) context.lookup("loja");
        MessageConsumer consumer = session.createConsumer(fila);

        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {

                TextMessage textMessage = (TextMessage) message;
                try {
                    var textPromotionDiasDasCriancas = "<promocao>Dia das criancas</promocao>";
                    String[] textPromotions = new String[]{
                            textPromotionDiasDasCriancas,
                            "<promocao>Dia dos avós</promocao>",
                            "<promocao>Dia dos avôs</promocao>",
                            "<promocao>Dia de ninguém</promocao>",
                    };

                    var messageText = textMessage.getText();

                    if (!messageText.contains("<promocao>Dia das criancas</promocao>")) return;

                    System.out.println(textMessage.getText());

                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

        });


        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}

