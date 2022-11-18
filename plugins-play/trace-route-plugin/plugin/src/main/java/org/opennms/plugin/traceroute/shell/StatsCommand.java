package org.opennms.plugin.traceroute.shell;

import java.util.concurrent.TimeUnit;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.opennms.plugin.traceroute.AlarmForwarder;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

@Command(scope = "opennms-trace-route-plugin", name = "stats", description = "Show statistics.")
@Service
public class StatsCommand implements Action {

    @Reference
    private AlarmForwarder forwarder;

    @Override
    public Object execute() {
        final MetricRegistry metrics = forwarder.getMetrics();
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.report();
        return null;
    }
}
