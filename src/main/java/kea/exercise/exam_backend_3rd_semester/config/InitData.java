package kea.exercise.exam_backend_3rd_semester.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class InitData implements CommandLineRunner {
    private final DataInitializationService dataInitializationService;

    public InitData(DataInitializationService dataInitializationService) {
        this.dataInitializationService = dataInitializationService;
    }

    @Override
    public void run(String... args) throws Exception {
        dataInitializationService.initializeData();
        System.out.println("Data initialized");
    }




}

