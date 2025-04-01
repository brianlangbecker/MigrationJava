# OpenTelemetry Demo Application

This is a simple Java application that demonstrates OpenTelemetry instrumentation using both automatic (Java agent) and manual instrumentation approaches in a single trace.

## Prerequisites

- Java 11 or later
- Maven
- curl (for downloading the OpenTelemetry Java agent)
- Honeycomb API key

## Features

The application demonstrates a complete trace that includes:

1. Automatic instrumentation via the Java agent
2. Manual instrumentation using:
   - OpenTelemetry API for direct span creation
   - Annotations (`@WithSpan` and `@SpanAttribute`)
3. Span attributes and events for better observability

All operations are connected in a single trace with the following hierarchy:

```
application-workflow
├── performWork (Task 1)
├── performWork (Task 2)
├── performWork (Task 3)
├── manual-operation (Manual Task)
└── annotated-operation (Annotated Task)
```

## Building the Application

```bash
mvn clean package
```

## Download OpenTelemetry Java Agent

```bash
curl -L https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar -o opentelemetry-javaagent.jar
```

## Running the Application

### Configure Honeycomb Environment Variables

First, set up your Honeycomb configuration:

```bash
export OTEL_EXPORTER_OTLP_ENDPOINT="https://api.honeycomb.io"
export OTEL_EXPORTER_OTLP_HEADERS="x-honeycomb-team=your-api-key"
export OTEL_SERVICE_NAME="MyJavaApp"
```

Replace `your-api-key` with your actual Honeycomb API key.

### Run the Application

To run the application with OpenTelemetry instrumentation sending data to Honeycomb:

```bash
java -javaagent:opentelemetry-javaagent.jar \
     -jar target/demo-1.0-SNAPSHOT.jar
```

This will:

1. Start the application with OpenTelemetry Java agent
2. Create a single trace containing all operations
3. Send telemetry data to Honeycomb using the configured environment variables
4. Use "MyJavaApp" as the service name in Honeycomb

## Implementation Details

The application demonstrates three instrumentation approaches:

1. Automatic Instrumentation with Annotations:

```java
@WithSpan
private static void performWork(@SpanAttribute("task.name") String taskName) {
    // Method implementation
}
```

2. Manual Instrumentation with Direct API:

```java
Span span = tracer.spanBuilder("manual-operation")
        .setAttribute("task.name", taskName)
        .setAttribute("instrumentation.type", "manual")
        .startSpan();
try {
    span.addEvent("Starting manual work");
    // Your code here
    span.addEvent("Manual work completed");
} finally {
    span.end();
}
```

3. Custom Named Spans with Attributes:

```java
@WithSpan("annotated-operation")
private static void performAnnotatedWork(@SpanAttribute("task.name") String taskName) {
    // Method implementation
}
```

You can view your traces in the Honeycomb UI by logging into your Honeycomb account. The trace will show the complete flow of the application with all operations properly connected in a single trace.
