package net.hytaledepot.templates.plugin.commands;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class CommandsDemoService {
  private final Map<String, AtomicLong> actionCounters = new ConcurrentHashMap<>();
  private final Map<String, String> lastActionBySender = new ConcurrentHashMap<>();
  private final Map<String, String> aliases = new ConcurrentHashMap<>();
  private volatile Path dataDirectory;

  public void initialize(Path dataDirectory) {
    this.dataDirectory = dataDirectory;
    aliases.clear();
    aliases.put("spawn", "/warp spawn");
    aliases.put("hub", "/warp hub");
  }

  public void onHeartbeat(long tick) {
    actionCounters.computeIfAbsent("heartbeat", key -> new AtomicLong()).incrementAndGet();

  }

  public void recordExternalEvent(String key) {
    actionCounters.computeIfAbsent(String.valueOf(key), item -> new AtomicLong()).incrementAndGet();
  }

  public String applyAction(CommandsPluginState state, String sender, String action, long heartbeatTicks) {
    String normalizedSender = String.valueOf(sender == null ? "unknown" : sender);
    String normalizedAction = normalizeAction(action);

    actionCounters.computeIfAbsent(normalizedAction, key -> new AtomicLong()).incrementAndGet();
    lastActionBySender.put(normalizedSender, normalizedAction);

    if ("toggle".equals(normalizedAction)) {
      boolean enabled = state.toggleDemoFlag();
      return "[Commands] demoFlag=" + enabled + ", heartbeatTicks=" + heartbeatTicks;
    }

    if ("info".equals(normalizedAction)) {
      return "[Commands] " + diagnostics();
    }

    String domainResult = handleDomainAction(normalizedSender, normalizedAction, heartbeatTicks);
    if (domainResult != null) {
      return "[Commands] " + domainResult;
    }

    return "[Commands] unknown action='" + normalizedAction + "' (try: info, toggle, sample, alias-check, register-alias, resolve-alias)";
  }

  public String describeLastAction(String sender) {
    return lastActionBySender.getOrDefault(String.valueOf(sender), "none");
  }

  public long operationCount() {
    long total = 0;
    for (AtomicLong value : actionCounters.values()) {
      total += value.get();
    }
    return total;
  }

  public String diagnostics() {
    String directory = dataDirectory == null ? "unset" : dataDirectory.toString();
    return "ops=" + operationCount()
        + ", aliases=" + aliases.size()
        + ", dataDirectory=" + directory;
  }

  public void shutdown() {
    aliases.clear();
  }

  private String handleDomainAction(String sender, String action, long heartbeatTicks) {
    if ("sample".equals(action) || "alias-check".equals(action)) {
      return "aliases=" + aliases;
    }
    if ("register-alias".equals(action)) {
      aliases.put("kit", "/player kit starter");
      return "registered alias kit -> /player kit starter";
    }
    if ("resolve-alias".equals(action)) {
      return "spawn resolves to " + aliases.getOrDefault("spawn", "missing");
    }
    return null;
  }

  private static String normalizeAction(String action) {
    String normalized = String.valueOf(action == null ? "" : action).trim().toLowerCase();
    return normalized.isEmpty() ? "sample" : normalized;
  }
}
