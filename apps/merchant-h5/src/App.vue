<template>
  <main class="page">
    <h1>{{ title }}</h1>
    <p class="subtitle">Merchant order dashboard starter.</p>
    <section class="card">
      <p>Shared helper says: <strong>{{ formatted }}</strong></p>
      <p>{{ greeting }}</p>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { formatAppName, greetUser } from '@shared/utils';

const title = 'Merchant H5';
const formatted = computed(() => formatAppName(title));
const greeting = greetUser('Merchant');
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 48px 24px;
  font-family: 'Inter', system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  background: radial-gradient(circle at 10% 20%, rgba(147, 197, 253, 0.2), #fff 25%),
    radial-gradient(circle at 90% 10%, rgba(129, 140, 248, 0.2), #fff 25%);
  color: #0f172a;
}

.subtitle {
  margin-top: 8px;
  color: #475569;
}

.card {
  margin-top: 24px;
  padding: 16px;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.08);
}
</style>
