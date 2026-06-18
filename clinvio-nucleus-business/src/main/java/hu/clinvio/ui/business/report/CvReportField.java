package hu.clinvio.ui.business.report;

public class CvReportField {
    private final String name;
    private final String label;
    private final FieldType type;
    private final boolean sortable;
    private final String format;

    private CvReportField(Builder builder) {
        this.name = builder.name;
        this.label = builder.label;
        this.type = builder.type;
        this.sortable = builder.sortable;
        this.format = builder.format;
    }

    public static Builder of(String name, String label) {
        return new Builder(name, label);
    }

    public String getName() { return name; }
    public String getLabel() { return label; }
    public FieldType getType() { return type; }
    public boolean isSortable() { return sortable; }
    public String getFormat() { return format; }

    public enum FieldType { TEXT, NUMBER, DATE, CURRENCY, BOOLEAN, STATUS }

    public static class Builder {
        private final String name;
        private final String label;
        private FieldType type = FieldType.TEXT;
        private boolean sortable = true;
        private String format;

        public Builder(String name, String label) { this.name = name; this.label = label; }
        public Builder type(FieldType type) { this.type = type; return this; }
        public Builder sortable(boolean sortable) { this.sortable = sortable; return this; }
        public Builder format(String format) { this.format = format; return this; }
        public CvReportField build() { return new CvReportField(this); }
    }
}
