package com.miso.menu.multiplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miso.menu.MultiplayerLobby;
import com.miso.menu.R;

/**
 * Created by michal.hornak on 10.05.2016.
 */
public class MultiplayerLobbyStateHandler {

    private MultiplayerLobby multiplayerLobby;

    Button mJoinButton;
    Button mReadyButton;
    Button mAbandonButton;
    Button mHostButton;
    Button mStartButton;
    EditText mPlayerNickname;
    TextView mTextInfo;

    public MultiplayerLobbyStateHandler(MultiplayerLobby multiplayerLobby) {
        this.multiplayerLobby = multiplayerLobby;
        bindViews();
    }

    private void bindViews() {
        this.mJoinButton = ((Button) this.multiplayerLobby.findViewById(R.id.button_join));
        this.mReadyButton = ((Button) this.multiplayerLobby.findViewById(R.id.button_ready));
        this.mAbandonButton = ((Button) this.multiplayerLobby.findViewById(R.id.button_abandon));
        this.mHostButton = ((Button) this.multiplayerLobby.findViewById(R.id.button_host));
        this.mStartButton = ((Button) this.multiplayerLobby.findViewById(R.id.button_start));
        this.mPlayerNickname = ((EditText) this.multiplayerLobby.findViewById(R.id.player_nickname));
        this.mTextInfo = ((TextView) this.multiplayerLobby.findViewById(R.id.textinfo_game_state_events));
    }

    public void joinClickUiEvents() {
        this.mJoinButton.setEnabled(false);
        this.mReadyButton.setEnabled(true);
        this.mAbandonButton.setEnabled(true);
        this.mHostButton.setEnabled(false);
        this.mPlayerNickname.setEnabled(false);

        this.mTextInfo.setText("Join successful!");
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
    }

    public void abandonClickUiEvents() {
        this.mJoinButton.setEnabled(true);
        this.mReadyButton.setEnabled(false);
        this.mAbandonButton.setEnabled(false);
        this.mHostButton.setEnabled(true);
        this.mPlayerNickname.setEnabled(true);
    }

    public void hostClickUiChanges() {
        this.mTextInfo.setText("Hosting Game!");
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_red_dark));
        this.mHostButton.setText("UN-HOST");
        this.mHostButton.setEnabled(false);
        this.mJoinButton.setEnabled(false);
        this.mPlayerNickname.setEnabled(false);
        hostAnimation();
    }

    public void unHostClickUiChanges() {
        this.mTextInfo.setText("Not hosting any game!");
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
        this.mHostButton.setText("HOST");
        this.mHostButton.setEnabled(false);
        this.mJoinButton.setEnabled(true);
        this.mStartButton.setEnabled(false);
        this.mPlayerNickname.setEnabled(true);

        unHostAnimation();
    }

    public void readyClickUiChanges() {
        this.mReadyButton.setText("UN-READY");
    }

    public void unReadyClickChanges() {
        this.mReadyButton.setText("READY");
    }

    public enum LobbyState {
        Default,
        Joined,
        JoinedAndReadyForGame,
        Hosting
    }

    private void unHostAnimation() {
        this.multiplayerLobby.findViewById(R.id.joined_players_row)
                .animate()
                .translationY(this.multiplayerLobby.findViewById(R.id.join_game_row).getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {

                        multiplayerLobby.findViewById(R.id.joined_players_row).setTranslationY(0);
                        multiplayerLobby.findViewById(R.id.join_game_row).setVisibility(View.VISIBLE);
                        multiplayerLobby.findViewById(R.id.join_game_row)
                                .animate()
                                .translationX(0)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mHostButton.setEnabled(true);
                                        multiplayerLobby.findViewById(R.id.multiplayer_lobby_layout).invalidate();
                                    }
                                });
                    }
                });
    }

    private void hostAnimation(){
        this.multiplayerLobby.findViewById(R.id.join_game_row)
                .animate()
                .translationX(-this.multiplayerLobby.findViewById(R.id.join_game_row).getWidth())
                .setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        multiplayerLobby.findViewById(R.id.joined_players_row)
                                .animate()
                                .translationY(-multiplayerLobby.findViewById(R.id.join_game_row).getHeight())
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mStartButton.setEnabled(true);
                                        mHostButton.setEnabled(true);
                                        multiplayerLobby.findViewById(R.id.joined_players_row).setTranslationY(0);
                                        multiplayerLobby.findViewById(R.id.join_game_row).setVisibility(View.GONE);
                                        multiplayerLobby.findViewById(R.id.multiplayer_lobby_layout).invalidate();
                                    }
                                });
                    }
                });
    }
}
