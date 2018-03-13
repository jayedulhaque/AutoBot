package com.mapler.nexmo;

import com.mapler.service.INotifier;
import com.mapler.service.ITask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.log4j.Logger;

/**
 *
 * @author JAYED
 */
public class NexmoSendWorker implements ITask {

    public ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    private NexmoModel nexmoModel;
    private static Logger log = Logger.getLogger(NexmoReadWorker.class);
    private INotifier iNotifier;

    public NexmoSendWorker(INotifier iNotifier, NexmoModel nexmoModel) {
        this.nexmoModel = nexmoModel;
        this.iNotifier = iNotifier;
    }

    @Override
    public void update() {
        try {
            iNotifier.notify("Sendbot:: Going to start working.");
            executor.execute(new NexmoSendClick(iNotifier, this.nexmoModel));
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.debug("Sendbot:: Error " + ex);
        }
    }

    @Override
    public void finish() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
