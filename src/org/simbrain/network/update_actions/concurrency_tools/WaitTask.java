package org.simbrain.network.update_actions.concurrency_tools;

import java.util.concurrent.BrokenBarrierException;

import edu.bonn.cs.net.jbarrier.barrier.AbstractBarrier;

public class WaitTask implements Task {

    private final AbstractBarrier syncBarrier;
    
    public WaitTask(AbstractBarrier syncBarrier) {
        this.syncBarrier = syncBarrier;
    }
    
    @Override
    public void perform(int id) throws InterruptedException,
            BrokenBarrierException {
        syncBarrier.await(id);
    }

}
