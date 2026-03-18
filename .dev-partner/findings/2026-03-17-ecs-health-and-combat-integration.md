---
title: Health and Combat systems follow ECS pattern but need careful state ordering
date: 2026-03-17
tags: [architecture, ecs, systems-design]
type: pattern
project: kalegar/ShardsOfTheInfinite
---

Added HealthSystem to process damage and removal, plus CombatSystem for attack logic. Both follow the established ECS pattern from the formations cleanup, which is solid.

Key observation: damage application and entity removal need strict ordering. If CombatSystem queues damage but HealthSystem removes the target in the same frame, rendering could break or removal logic could access stale component data. Ensure:

1. CombatSystem applies damage first, HealthSystem processes removal second
2. If using immediate removal, cascade checks for in-flight attacks targeting dead entities
3. Health bar rendering should skip entities queued for removal or already dead

The health bar in UnitRenderSystem is a good call—visual feedback on damage is critical for combat feel. Just make sure it doesn't render for entities between death and removal, or you'll get visual glitches.
