export function formatAppName(name: string): string {
  return name.trim().toUpperCase();
}

export function greetUser(name: string): string {
  const cleaned = name.trim();
  return cleaned ? `Hello, ${cleaned}!` : 'Hello!';
}
