# https://stackoverflow.com/a/52819989
FROM rabbitmq:3.7-management

# https://dl.bintray.com/rabbitmq/community-plugins
RUN apt-get update && apt-get install -y curl unzip \
    && curl https://dl.bintray.com/rabbitmq/community-plugins/3.7.x/rabbitmq_delayed_message_exchange/rabbitmq_delayed_message_exchange-20171201-3.7.x.zip > rabbitmq_delayed_message_exchange-20171201-3.7.x.zip \
    && unzip rabbitmq_delayed_message_exchange-20171201-3.7.x.zip && rm -f rabbitmq_delayed_message_exchange-20171201-3.7.x.zip \
    && mv rabbitmq_delayed_message_exchange-20171201-3.7.x.ez plugins \
    && rm -rf /var/lib/apt/lists/*
RUN rabbitmq-plugins enable rabbitmq_delayed_message_exchange

# docker build -t rabbitmq:3.7-management-delayed .
