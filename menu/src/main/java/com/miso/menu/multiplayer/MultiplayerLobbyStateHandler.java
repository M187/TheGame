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

    private Button mJoinButton;
    private Button mMainButton1;
    private Button mMainButton2;
    private EditText mPlayerNickname;
    private TextView mTextInfo;

    public enum LobbyState {
        Default,
        Joined,
        JoinedAndReadyForGame,
        Hosting
    }

    public MultiplayerLobbyStateHandler(MultiplayerLobby multiplayerLobby) {
        this.multiplayerLobby = multiplayerLobby;
        bindViews();
    }

    private void bindViews() {
        this.mJoinButton = ((Button) this.multiplayerLobby.findViewById(R.id.button_join));
        this.mMainButton1 = ((Button) this.multiplayerLobby.findViewById(R.id.button_main_1));
        this.mMainButton2 = ((Button) this.multiplayerLobby.findViewById(R.id.button_main_2));
        this.mPlayerNickname = ((EditText) this.multiplayerLobby.findViewById(R.id.player_nickname));
        this.mTextInfo = ((TextView) this.multiplayerLobby.findViewById(R.id.textinfo_game_state_events));
    }

    public void joinClickUiEvents() {
        this.mJoinButton.setEnabled(false);
        this.mMainButton1.setEnabled(false);
        this.mPlayerNickname.setEnabled(false);

        this.mTextInfo.setText(R.string.join_message);
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));

        bindJoinEventsToMainButtons();
        hideJoinRowAnimation();
    }

    public void abandonClickUiEvents() {
        this.mTextInfo.setText(R.string.abandon_message);
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
        this.mJoinButton.setEnabled(true);
        this.mMainButton1.setEnabled(false);
        this.mMainButton2.setEnabled(false);
        this.mPlayerNickname.setEnabled(true);

        unbindJoinEventsToMainButtons();
        showJoinRowAnimation();
    }

    public void hostClickUiChanges() {
        this.mTextInfo.setText(R.string.host_message);
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_red_dark));
        this.mMainButton1.setText(R.string.unhost_message);
        this.mMainButton1.setEnabled(false);
        this.mJoinButton.setEnabled(false);
        this.mPlayerNickname.setEnabled(false);

        hideJoinRowAnimation();
    }

    public void unHostClickUiChanges() {
        this.mTextInfo.setText(R.string.unhost_message);
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
        this.mMainButton1.setText(R.string.host);
        this.mMainButton1.setEnabled(false);
        this.mJoinButton.setEnabled(true);
        this.mMainButton2.setEnabled(false);
        this.mPlayerNickname.setEnabled(true);

        showJoinRowAnimation();
    }

    public void readyClickUiChanges() {
        this.mMainButton2.setText(R.string.unready);
    }

    public void unReadyClickChanges() {
        this.mMainButton2.setText(R.string.ready);
    }

    private void showJoinRowAnimation() {
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
                                        mMainButton1.setEnabled(true);
                                        multiplayerLobby.findViewById(R.id.multiplayer_lobby_layout).invalidate();
                                    }
                                });
                    }
                });
    }

    private void hideJoinRowAnimation(){
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
                                        mMainButton2.setEnabled(true);
                                        mMainButton1.setEnabled(true);
                                        multiplayerLobby.findViewById(R.id.joined_players_row).setTranslationY(0);
                                        multiplayerLobby.findViewById(R.id.join_game_row).setVisibility(View.GONE);
                                        multiplayerLobby.findViewById(R.id.multiplayer_lobby_layout).invalidate();
                                    }
                                });
                    }
                });
    }

    private void bindJoinEventsToMainButtons(){
        this.mMainButton1.setText(R.string.abandon);
        this.mMainButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                multiplayerLobby.abandonClick(v);
            }
        });

        this.mMainButton2.setText(R.string.ready);
        this.mMainButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplayerLobby.readyClick(v);
            }
        });
    }

    private void unbindJoinEventsToMainButtons(){
        this.mMainButton1.setText(R.string.host);
        this.mMainButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                multiplayerLobby.hostClick(v);
            }
        });

        this.mMainButton2.setText(R.string.start);
        this.mMainButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplayerLobby.startGame(v);
            }
        });
    }
}
