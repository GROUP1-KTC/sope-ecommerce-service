#!/bin/sh
set -e

echo "⏳ Waiting for Elasticsearch to start..."
until curl -s http://sope-elasticsearch:9200 >/dev/null; do
  sleep 2
done

echo "🚀 Creating products_index with custom analyzer..."
curl -X PUT "http://sope-elasticsearch:9200/products_index" \
  -H 'Content-Type: application/json' \
  -d @/products_index_template.json || true

echo "🚀 Creating product_click_logs index..."
curl -X PUT "http://sope-elasticsearch:9200/product_click_logs" \
  -H 'Content-Type: application/json' \
  -d @/product_click_logs.json || true

echo "✅ All indices initialized!"
