<template>
  <main class="page">
    <h1>{{ title }}</h1>
    <p class="subtitle">A lightweight user-facing H5 shell powered by Vite.</p>
    <section class="card">
      <h2>Shared utilities</h2>
      <p>The formatted app name is: <strong>{{ formatted }}</strong></p>
      <p>{{ greeting }}</p>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { formatAppName, greetUser } from '@shared/utils';

const title = 'User H5';
const formatted = computed(() => formatAppName(title));
const greeting = greetUser('User');
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 48px 24px;
  font-family: 'Inter', system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  background: radial-gradient(circle at top, #f1f5ff 0, #f8fafc 45%, #ffffff 100%);
  color: #111827;
}

.subtitle {
  margin-top: 8px;
  color: #4b5563;
}

.card {
  margin-top: 24px;
  padding: 16px;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.08);
}
</style>
