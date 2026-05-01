package com.jobtracker.application.model.enums;

import java.util.EnumSet;
import java.util.Set;


public enum Status {
    IN_PROGRESS {
        @Override
        public Set<Status> getAllowedTransitions() {
            return EnumSet.of(APPLIED);
        }
    },

    APPLIED {
        @Override
        public Set<Status> getAllowedTransitions() {
            return EnumSet.of(INTERVIEW, WITHDRAWN);
        }
    },

    INTERVIEW {
        @Override
        public Set<Status> getAllowedTransitions() {
            return EnumSet.of(OFFER, REJECTED);
        }
    },

    OFFER {
        @Override
        public Set<Status> getAllowedTransitions() {
            return EnumSet.of(WITHDRAWN);
        }
    },

    REJECTED {
        @Override
        public Set<Status> getAllowedTransitions() {
            return EnumSet.noneOf(Status.class);
        }
    },

    WITHDRAWN {
        @Override
        public Set<Status> getAllowedTransitions() {
            return EnumSet.noneOf(Status.class);
        }
    };

    public abstract Set<Status> getAllowedTransitions();

    public boolean canTransitionTo(Status status) {
        return getAllowedTransitions().contains(status);
    }
}


