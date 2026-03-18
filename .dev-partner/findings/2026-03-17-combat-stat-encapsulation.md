---
title: Combat component should isolate attack mechanics from health to avoid tight coupling
date: 2026-03-17
tags: [component-design, separation-of-concerns]
type: gotcha
project: kalegar/ShardsOfTheInfinite
---

With CombatComponent handling attack stats and HealthSystem handling damage intake, there's a natural seam here. Make sure CombatSystem doesn't directly mutate Health—pass damage events or use a damage queue instead.

Reasoning: if combat stats later include modifiers (armor, buffs, status effects), or if you want to log/replay damage, direct mutation becomes a liability. Keep the contract simple: CombatSystem calculates final damage, posts it; HealthSystem applies and notifies.

Also watch the entity reference in CombatComponent—if it stores target references, clean them up when targets die, or you'll leak entity references and break selection/AI targeting later.
