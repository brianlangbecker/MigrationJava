package com.example.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;

public class DemoApplication {
    private static final Tracer tracer = GlobalOpenTelemetry.getTracer("demo-app");

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Demo Application");

        // Let the Java agent create the root span automatically
        doWork();

        System.out.println("\nDemo Application Completed");
    }

    @WithSpan("application-workflow")
    private static void doWork() throws InterruptedException {
        System.out.println("\n=== Starting Work Process ===");

        // Automatic instrumentation examples
        performWork("Task 1");
        performWork("Task 2");
        performWork("Task 3");

        // Manual instrumentation examples
        performManualWork("Manual Task");
        performAnnotatedWork("Annotated Task");

        // Give some time for the spans to be exported
        Thread.sleep(1000);
    }

    @WithSpan
    private static void performWork(@SpanAttribute("task.name") String taskName) throws InterruptedException {
        System.out.println("Performing " + taskName + "...");
        Thread.sleep(500);
        System.out.println(taskName + " completed!");
    }

    private static void performManualWork(String taskName) throws InterruptedException {
        Span span = tracer.spanBuilder("manual-operation")
                .setAttribute("task.name", taskName)
                .setAttribute("instrumentation.type", "manual")
                .startSpan();

        span.addEvent("Starting manual work");
        try {
            System.out.println("Performing " + taskName + "...");
            Thread.sleep(400);
            System.out.println(taskName + " completed!");
            span.addEvent("Manual work completed");
        } finally {
            span.end();
        }
    }

    @WithSpan("annotated-operation")
    private static void performAnnotatedWork(@SpanAttribute("task.name") String taskName) throws InterruptedException {
        System.out.println("Performing " + taskName + "...");
        Thread.sleep(300);
        System.out.println(taskName + " completed!");
    }
}