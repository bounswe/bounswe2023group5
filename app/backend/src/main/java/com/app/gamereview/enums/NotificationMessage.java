package com.app.gamereview.enums;

import lombok.Getter;

    @Getter
    public enum NotificationMessage {
        FIRST_POST_ACHIEVEMENT("Way to go, {user_name}! 🎉 You've unlocked the 'First Post' achievement by starting a discussion in the {forum_name} forum. Your contributions make our community vibrant!"),
        FIRST_REVIEW_ACHIEVEMENT("Awesome work, {user_name}! 🏆 You've achieved the 'First Review' milestone by sharing your take on {game_name} game. Your opinions help others make great choices!"),
        FIRST_VOTE_ACHIEVEMENT("Congratulations, {user_name}! 🗳️ You've earned the 'First Vote' badge by actively participating in community voting. Your valuable input significantly influences our community's development!"),
        FIRST_COMMENT_ACHIEVEMENT("Great job, {user_name}! 💬 You've earned the 'First Comment' recognition by engaging in discussions. Your thoughts on '{post_title}' contribute to our community conversations!"),
        NEW_POST_IN_PRIVATE_GROUP("Hey {user_name}! 🌟 There's something new for you. A fresh post has just landed in your private group, '{group_title}'. Check it out and join the conversation. We can't wait to hear your thoughts!"),
        FIRST_VOTE_OF_THE_POST("Hey {user_name}! 📣 Your post '{post_title}' just got its first vote. Check it out and see what others think about your post!"),
        NTH_VOTE_OF_THE_POST("Exciting news {user_name}! 📣 Your post '{post_title}' just reached {overall_vote} overall vote. It's great to see your contributions resonating with the community. Keep up the fantastic work and keep sharing your thoughts!"),
        NEW_COMMENT_FOR_THE_POST("Good news, {user_name}! 🌟 Your post '{post_title}' has a new comment. It's great to see your contributions sparking conversations in the community. Dive in and join the discussion!"),
        NEW_REPLY_FOR_THE_COMMENT("Hey {user_name}! 🌟 Your comment on '{post_title}' has a new reply. It's great to see your contributions sparking conversations in the community. Dive in and join the discussion!"),
        BANNED_FROM_GROUP("Important Notice, {user_name}! 🚫 You have been banned from the '{group_title}' group. This may be due to a violation of our community guidelines or group rules. If you believe this is a mistake, you can contact the moderators for further clarification."),
        APPLICATION_ACCEPTED("Congratulations, {user_name}! 🎉 Your application to join the private group '{group_title}' has been accepted. Welcome to the group! Dive in and start engaging with your new community."),
        APPLICATION_REJECTED("Notification for {user_name}, 🚫 Your application to join the private group '{group_title}' has been reviewed and unfortunately, it has not been accepted at this time. This decision is based on our group criteria and community guidelines. We encourage you to review these guidelines and consider applying again in the future."),
        NEW_GROUP_APPLICATION("Attention Moderators! 🌟 There is a new application for the private group '{group_title}'. Applicant: {user_name}. Please review the application and take appropriate action to maintain the integrity and quality of our group community.");
        private final String messageTemplate;
    NotificationMessage(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }
}
