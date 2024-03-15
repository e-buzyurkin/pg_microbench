## Запросы:

| Запросы                 | Приоритетность | Готовность | Статус |
| ----------------------- | -------------- | ---------- | ------ |
| 1. Result               |                | ✖   |        |
| 2. Values Scan          |                | ✅          |        |
| 3. Function Scan        |                | ✅          |        |
| 4. Incremental Sort     |                | ✖   |        |
| 5. Unique               |                | ✖   |        |
| 6. Append               |                | ✅          |        |
| 7. Merge Append         |                | ✖   |        |
| 8. Subquery Scan        |                | ✖   |        |
| 9. HashSetOp            |                | ✖   |        |
| 10. SetOp               |                | ✖   |        |
| 11. Materialize         |                | ✖   |        |
| 12. Memoize             |                | ✖   |        |
| 13. Group               |                | ✖   |        |
| 14. Aggregate           |                | ✅          |        |
| 15. GroupAggregate      |                | ✖   |        |
| 16. HashAggregate       |                | ✖   |        |
| 17. MixedAggregate      |                | ✖   |        |
| 18. WindowAgg           |                | ✅          |        |
| 19. Parallel Seq Scan   |                | ✖   |        |
| 20. Gather              |                | ✅          |        |
| 21. Finalize Aggregate  |                | ✖   |        |
| 22. Gather Merge        |                | ✖   |        |
| 23. Parallel Append     |                | ✖   |        |
| 24. Parallel Hash       |                | ✖   |        |
| 25. Parallel Hash Join  | ✅              | ✖   |        |
| 26. CTE Scan            |                | ✖   |        |
| 27. WorkTable Scan      |                | ✖   |        |
| 28. WorkTable Scan      |                | ✖   |        |
| 29. Recursive Union     |                | ✖   |        |
| 30. ProjectSet          |                | ✖   |        |
| 31. LockRows            |                | ✖   |        |
| 32. Sample Scan         |                | ✖   |        |
| 33. Table Function Scan |                | ✖   |        |
| 34. Foreign Scan        |                | ✖   |        |
| 35. Tid Scan            |                | ✖   |        |
| 36. Insert              |                | ✖   |        |
| 37. Update              |                | ✖   |        |
| 38. Delete              |                | ✖   |        |
| 39. Merge               |                | ✖   |        |
| 40. Semi Join           | ✅              | ✖   |        |
| 41. Anti Join           | ✅              | ✖   |        |
| 42. SubPlan             | ✅              | ✖   |        |

## Действия:

| Действие                 | Статус |
|--------------------------|-------|
 | Запуск всех тестов       |    ✖    |
  | Проверка на корректность плана |    ✖    |
| Сделать удобное лог      |  ✖   |
   