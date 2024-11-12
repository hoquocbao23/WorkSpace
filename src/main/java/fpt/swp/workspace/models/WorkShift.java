package fpt.swp.workspace.models;

import java.time.LocalTime;

public enum WorkShift {

        MORNING(LocalTime.of(6, 0), LocalTime.of(15, 0)), // 6:00 - 11:00
        EVENING(LocalTime.of(15, 0), LocalTime.of(23, 0)), // 11:00 - 23:00
        FULL_TIME(LocalTime.of(6, 0), LocalTime.of(23, 0));

        private final LocalTime startTime;
        private final LocalTime endTime;

        WorkShift(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }


}
