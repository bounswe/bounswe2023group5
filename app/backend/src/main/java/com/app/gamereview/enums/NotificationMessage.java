package com.app.gamereview.enums;

import lombok.Getter;

    @Getter
    public enum NotificationMessage {
        FIRST_POST_ACHIEVEMENT("Way to go, {user_name}! 🎉 You've unlocked the 'First Post' achievement by starting a discussion in the {forum_name} forum. Your contributions make our community vibrant!"),
        FIRST_REVIEW_ACHIEVEMENT("Awesome work, {user_name}! 🏆 You've achieved the 'First Review' milestone by sharing your take on {game_name} game. Your opinions help others make great choices!"),
        FIRST_VOTE_ACHIEVEMENT("Congratulations, {user_name}! 🗳️ You've earned the 'First Vote' badge by actively participating in community voting. Your valuable input significantly influences our community's development!"),
        FIRST_COMMENT_ACHIEVEMENT("Great job, {user_name}! 💬 You've earned the 'First Comment' recognition by engaging in discussions. Your thoughts on '{post_title}' contribute to our community conversations!");
        private final String messageTemplate;
    NotificationMessage(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }


}
