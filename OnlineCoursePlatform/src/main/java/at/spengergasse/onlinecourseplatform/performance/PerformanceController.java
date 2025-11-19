package at.spengergasse.onlinecourseplatform.performance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for triggering performance tests
 */
@RestController
@RequestMapping("/api/performance")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class PerformanceController {

    private final PerformanceTestService performanceTestService;

    /**
     * Run all performance tests
     * POST /api/performance/run
     */
    @PostMapping("/run")
    public ResponseEntity<List<PerformanceResult>> runPerformanceTests() {
        log.info("Starting performance tests via API");
        List<PerformanceResult> results = performanceTestService.runAllTests();
        return ResponseEntity.ok(results);
    }

    /**
     * Get test status
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Performance testing service is ready");
    }
}
