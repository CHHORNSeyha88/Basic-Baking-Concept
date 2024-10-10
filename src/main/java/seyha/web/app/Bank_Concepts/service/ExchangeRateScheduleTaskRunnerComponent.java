package seyha.web.app.Bank_Concepts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ExchangeRateScheduleTaskRunnerComponent implements CommandLineRunner {

    // Use org.slf4j.Logger instead of java.util.logging.Logger
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduleTaskRunnerComponent.class);

    private final ExchangeRatesService xchangeRateService;
    private final ScheduledExecutorService scheduler;

    public ExchangeRateScheduleTaskRunnerComponent(ExchangeRatesService xchangeRateService, ScheduledExecutorService scheduler) {
        this.xchangeRateService = xchangeRateService;
        this.scheduler = scheduler;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Calling The Currency API endpoint for exchange rate");
        scheduler.scheduleWithFixedDelay(xchangeRateService::getExchangeService, 0, 12, TimeUnit.HOURS);
        logger.info("End Calling The Currency API endpoint for exchange rate");
    }
}
