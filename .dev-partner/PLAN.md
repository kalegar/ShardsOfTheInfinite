# PLAN

## Current Focus: Unit Combat System

### Completed
- [x] HealthComponent with damage/heal/death tracking
- [x] HealthSystem to process deaths and remove entities
- [x] HealthBarRenderer for visual feedback (color-coded by health %)
- [x] CombatComponent with attack stats and cooldown
- [x] CombatSystem for range-checked attack processing

### Next Steps
- [ ] Wire HealthSystem and CombatSystem into the engine (priority ordering)
- [ ] Integrate HealthBarRenderer into existing render pipeline
- [ ] Add target acquisition logic (nearest enemy, or click-to-target)
- [ ] Add attack animations or visual feedback on hit
- [ ] Add armor/defense modifier to damage calculation
- [ ] Unit death VFX and cleanup (deselect dead units, etc.)
- [ ] Consider projectile entities for ranged attacks

### Backlog
- Formation movement improvements
- Pathfinding (A* or flow fields)
- Fog of war
- Minimap
- Resource system
