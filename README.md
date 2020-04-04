# LCEDF Simulator
[논문 링크](https://doi.org/10.3390/sym12010172)

실시간 시스템에서 각 task는 deadline 전에 실행을 완료해야 하며, EDF (Earliest Deadline First)는 주어진 task set의 타이밍 제약조건을 충족시키기 위해 가장 널리 사용되는 스케줄 알고리즘 중 하나이다. 그러나 EDF는 시스템이 미래의 task release 패턴을 알지 못하는 경우 선점이 허용되지 않는 non-preemptive task set에 대한 타이밍 제약 조건을 충족시키는 데 효과적이지 않은 것으로 알려져 있다. 본 논문에서는 멀티프로세서가 실시간 시스템에서 non-preemptive task의 제한된 미래 release 패턴 정보만을 요구하는 스케줄링 알고리즘 LCEDF (Limitedly Clairvoyant non-preemptive EDF)를 개발한다. 그런 다음 멀티프로세서 실시간 시스템에서 non-preemptive task set의 타이밍 제약 조건 충족을 보장하는 schedulability analysis를 도출한다. 그리고 시뮬레이션을 통해 LCEDF의 schedulability analysis를 미래 release 패턴에 대한 정보가 없는 vanilla non-preemptive EDF의 schedulability analysis와 비교하여 non-preemptive task set의 타이밍 제약 조건을 충족시키는 성능이 크게 향상되었음을 보여준다.
