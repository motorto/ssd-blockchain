package group3.ssd.blockchain.p2p.Operation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public interface Operation {

    static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    public void execute();

    public boolean hasFinished();

}