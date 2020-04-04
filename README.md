# LCEDF Simulator
[Paper Link](https://doi.org/10.3390/sym12010172)

## Abstract
In a real-time system, a series of jobs invoked by each task should finish its execution before its deadline, and EDF (Earliest Deadline First) is one of the most popular scheduling algorithms to meet such timing constraints of a set of given tasks. However, EDF is known to be ineffective in meeting timing constraints for non-preemptive tasks (which disallow any preemption) when the system does not know the future job release patterns of the tasks. In this paper, we develop a scheduling algorithm for a real-time system with a symmetry multiprocessor platform, which requires only limited information about the future job release patterns of a set of non-preemptive
tasks, called LCEDF. We then derive its schedulability analysis that provides timing guarantees of the non-preemptive task set on a symmetry multiprocessor platform. Via simulations, we demonstrate the proposed schedulability analysis for LCEDF significantly improves the schedulability performance in meeting timing constraints of a set of non-preemptive tasks up to 20.16%, compared to vanilla
non-preemptive EDF.
