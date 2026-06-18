package hu.clinvio.ui.business.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for exporting data to CSV format.
 *
 * <p>Use in controller methods:</p>
 * <pre>{@code
 * @GetMapping("/export")
 * public void exportOrders(HttpServletResponse response) {
 *     List<Order> orders = orderService.findAll();
 *     CsvExport.builder(response, "orders")
 *         .addHeader("Order #", Order::getOrderNumber)
 *         .addHeader("Customer", Order::getCustomerName)
 *         .addHeader("Total", o -> o.getTotalAmount().toString())
 *         .addHeader("Status", o -> o.getStatus().getLabel())
 *         .addHeader("Created", o -> o.getCreatedAt().toString())
 *         .write(orders);
 * }
 * }</pre>
 */
public class CsvExport {

    private CsvExport() {}

    /**
     * Create a CSV export builder.
     */
    public static <T> Builder<T> builder(HttpServletResponse response, String filename) {
        return new Builder<>(response, filename);
    }

    /**
     * Builder for CSV export.
     */
    public static class Builder<T> {
        private final HttpServletResponse response;
        private final String filename;
        private final StringBuilder header = new StringBuilder();
        private final List<Function<T, String>> extractors = new java.util.ArrayList<>();

        public Builder(HttpServletResponse response, String filename) {
            this.response = response;
            this.filename = filename;
        }

        /**
         * Add a column header.
         */
        public Builder<T> addHeader(String name, Function<T, String> valueExtractor) {
            if (!header.isEmpty()) {
                header.append(",");
            }
            header.append(escapeCsv(name));
            extractors.add(valueExtractor);
            return this;
        }

        /**
         * Write the CSV to the response.
         */
        public void write(List<T> data) throws IOException {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + filename + "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                    ".csv\"");

            PrintWriter writer = response.getWriter();
            writer.println(header.toString());

            for (T item : data) {
                StringBuilder row = new StringBuilder();
                boolean first = true;
                for (Function<T, String> extractor : extractors) {
                    if (!first) row.append(",");
                    String value = extractor != null ? extractor.apply(item) : "";
                    row.append(escapeCsv(value != null ? value : ""));
                    first = false;
                }
                writer.println(row.toString());
            }

            writer.flush();
        }
    }

    /**
     * Write a simple CSV with headers and rows.
     */
    public static void writeCsv(HttpServletResponse response, String filename,
                                 List<String> headers, List<List<String>> rows) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + filename + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                ".csv\"");

        PrintWriter writer = response.getWriter();

        // Write headers
        writer.println(headers.stream()
                .map(CsvExport::escapeCsv)
                .collect(Collectors.joining(",")));

        // Write rows
        for (List<String> row : rows) {
            writer.println(row.stream()
                    .map(CsvExport::escapeCsv)
                    .collect(Collectors.joining(",")));
        }

        writer.flush();
    }

    /**
     * Escape a CSV value (handle commas, quotes, newlines).
     */
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
