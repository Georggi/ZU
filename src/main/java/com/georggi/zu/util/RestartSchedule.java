package com.georggi.zu.util;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.georggi.zu.ZU;

public class RestartSchedule {

    public int secondsToRestart;

    public RestartSchedule(String schedule) {
        // Parse cron expression from schedule string
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);
        CronParser parser = new CronParser(cronDefinition);
        Cron cron = parser.parse(schedule);
        ZU.LOG.info("Cron schedule: {}", cron.asString());

        ZonedDateTime now = ZonedDateTime.now();
        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);
        ZU.LOG.info("Next restart at: {}", nextExecution.orElse(null));
        Optional<Duration> timeToNextExecution = executionTime.timeToNextExecution(now);
        secondsToRestart = (int) timeToNextExecution.orElse(Duration.ZERO)
            .getSeconds();
    }
}
