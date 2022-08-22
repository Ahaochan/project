package moe.ahao.tend.consistency.core.adapter.subscribe;

import moe.ahao.tend.consistency.core.adapter.message.ReShardTaskAndReElectionEvent;
import moe.ahao.tend.consistency.core.election.PeerElectionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReShardTaskAndReElectionSubscribe {
    @Autowired
    private PeerElectionHandler peerElectionHandler;

    @EventListener(ReShardTaskAndReElectionEvent.class)
    public void reShardTaskAndReElection(ReShardTaskAndReElectionEvent event) {
        peerElectionHandler.reShardTaskAndReElection();
    }
}
