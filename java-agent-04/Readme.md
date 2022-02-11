# README

This agent export jvm metrics to prometheus through http://localhost:18888/metircs

if you want to see the metrics in prometheus, add the job in prometheus.yml
```yaml
  - job_name: 'jvm-demo'
    static_configs:
      - targets: ['localhost:18888']
    metrics_path: "/metrics"
```