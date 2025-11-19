package at.spengergasse.onlinecourseplatform.performance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the result of a performance test
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceResult {

    private String operation; // e.g., "INSERT", "FIND_ALL", "UPDATE"
    private String database; // "MySQL" or "MongoDB"
    private int recordCount; // Number of records involved
    private long durationMs; // Duration in milliseconds
    private LocalDateTime timestamp;
    private String details; // Additional details about the operation

    @Override
    public String toString() {
        return String.format("[%s] %s on %s: %d records in %d ms (%s)",
                timestamp, operation, database, recordCount, durationMs, details);
    }
}
