package moe.ahao.spring.boot.mongdb;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MongoDBExtension implements BeforeAllCallback, AfterAllCallback {
    private static MongodExecutable mongodExecutable;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodConfig mongodConfig = MongodConfig.builder()
            .version(Version.Main.PRODUCTION)
            .net(new Net("127.0.0.1", 27017, false))
            .build();

        mongodExecutable = starter.prepare(mongodConfig);
        MongodProcess mongodProcess = mongodExecutable.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }
}
