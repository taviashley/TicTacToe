package com.example.tictactoe

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    enum class ActivePlayers {
        CIRCLE,      // CIRCLE O
        CROSS      // CROSS X
    }

    enum class WINNER {
        CIRCLE, // CIRCLE O
        CROSS, // CROSS X
        DRAW
    }

    private var activePlayer: ActivePlayers? = null
    private var winner: WINNER? = null

    private var playerOneSelections: ArrayList<Int> =
        ArrayList()       // To track selections for winning logic
    private var playerTwoSelections: ArrayList<Int> =
        ArrayList()       // To track selections for winning logic
    private var disabledImages: ArrayList<ImageButton?> =
        ArrayList()   // To track all selections (for both players)

    private var mBtnReset: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mBtnReset = findViewById(R.id.button_resetGame)
        // Let the CIRCLE player start
        activePlayer = ActivePlayers.CIRCLE

        clickResetGame()
    }

    // Runs after clickImageButton, performs logic of clicking image button
    private fun performAction(number: Int, imageButton: ImageButton) {
        if (activePlayer == ActivePlayers.CIRCLE) {
            imageButton.setImageResource(R.drawable.ic_circle_black_24dp)
            imageButton.isEnabled = false
            playerOneSelections.add(number)
            disabledImages.add(imageButton)
            activePlayer = ActivePlayers.CROSS
            binding.textViewCurrentPlayer.text = getString(R.string.circle_x)
            winnerCheck(playerOneSelections, WINNER.CIRCLE)
        } else if (activePlayer == ActivePlayers.CROSS) {
            imageButton.setImageResource(R.drawable.ic_cross_black_24dp)
            imageButton.isEnabled = false
            playerTwoSelections.add(number)
            disabledImages.add(imageButton)
            activePlayer = ActivePlayers.CIRCLE
            binding.textViewCurrentPlayer.text = getString(R.string.circle_o)
            winnerCheck(playerTwoSelections, WINNER.CROSS)
        }
    }

    // Winning logic
    private fun winnerCheck(selections: ArrayList<Int>, win: WINNER) {
        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
        if (selections.contains(7)) {
            if (selections.contains(8)) {
                if (selections.contains(9)) {
                    winner = win                // top row win
                    binding.imageButton7.setColorFilter(Color.GREEN)
                    binding.imageButton8.setColorFilter(Color.GREEN)
                    binding.imageButton9.setColorFilter(Color.GREEN)
                }
            }
            if (selections.contains(4)) {
                if (selections.contains(1)) {
                    winner = win                // left column win
                    binding.imageButton7.setColorFilter(Color.GREEN)
                    binding.imageButton4.setColorFilter(Color.GREEN)
                    binding.imageButton1.setColorFilter(Color.GREEN)
                }
            }
            if (selections.contains(5)) {
                if (selections.contains(3)) {
                    winner = win                // diagonal top left to bottom right win
                    binding.imageButton7.setColorFilter(Color.GREEN)
                    binding.imageButton5.setColorFilter(Color.GREEN)
                    binding.imageButton3.setColorFilter(Color.GREEN)
                }
            }
        }
        if (selections.contains(5)) {
            if (selections.contains(4)) {
                if (selections.contains(6)) {
                    winner = win                // middle row win
                    binding.imageButton5.setColorFilter(Color.GREEN)
                    binding.imageButton4.setColorFilter(Color.GREEN)
                    binding.imageButton6.setColorFilter(Color.GREEN)
                }
            }
            if (selections.contains(8)) {
                if (selections.contains(2)) {
                    winner = win                // middle column win
                    binding.imageButton5.setColorFilter(Color.GREEN)
                    binding.imageButton8.setColorFilter(Color.GREEN)
                    binding.imageButton2.setColorFilter(Color.GREEN)
                }
            }
            if (selections.contains(9)) {
                if (selections.contains(1)) {
                    winner = win                // diagonal top right to bottom left win
                    binding.imageButton5.setColorFilter(Color.GREEN)
                    binding.imageButton9.setColorFilter(Color.GREEN)
                    binding.imageButton1.setColorFilter(Color.GREEN)
                }
            }
        }
        if (selections.contains(3)) {
            if (selections.contains(2)) {
                if (selections.contains(1)) {
                    winner = win                // bottom row win
                    binding.imageButton3.setColorFilter(Color.GREEN)
                    binding.imageButton2.setColorFilter(Color.GREEN)
                    binding.imageButton1.setColorFilter(Color.GREEN)
                }
            }
            if (selections.contains(6)) {
                if (selections.contains(9)) {
                    winner = win                // right column win
                    binding.imageButton3.setColorFilter(Color.GREEN)
                    binding.imageButton6.setColorFilter(Color.GREEN)
                    binding.imageButton9.setColorFilter(Color.GREEN)
                }
            }
        }

        // Check if anyone won or draw game
        if (winner == WINNER.CIRCLE) {
            createWinnerAlert(
                getString(R.string.circle_o_wins),
                getString(R.string.congrats),
                AlertDialog.BUTTON_POSITIVE,
                getString(R.string.ok),
                false
            )
        } else if (winner == WINNER.CROSS) {
            createWinnerAlert(
                getString(R.string.cross_x_wins),
                getString(R.string.congrats),
                AlertDialog.BUTTON_POSITIVE,
                getString(R.string.ok),
                false
            )
        } else if (disabledImages.size == 9) {
            createWinnerAlert(
                getString(R.string.game_draw),
                getString(R.string.lose),
                AlertDialog.BUTTON_POSITIVE,
                getString(R.string.ok),
                false
            )
        }
    }

    // Display winner
    private fun createWinnerAlert(
        title: String,
        message: String,
        buttonType: Int,
        buttonText: String,
        cancelable: Boolean
    ) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setCancelable(cancelable)

        alertDialog.setButton(buttonType, buttonText) { dialog: DialogInterface?, which: Int ->
            null
        }
        binding.textViewCurrentPlayerTitle.text = getString(R.string.game_over)
        binding.textViewCurrentPlayer.text = title
        alertDialog.show()
    }

    // Called when clicking image button (i.e the tic tac toe spaces)
    fun clickImageButton(view: View) {
        // Only allow logic processing if nobody has won and not draw
        if (winner == null) {
            val selectedImageButton: ImageButton = view as ImageButton

            var number: Int = 0

            when (selectedImageButton.id) {
                R.id.imageButton_1 -> number = 1
                R.id.imageButton_2 -> number = 2
                R.id.imageButton_3 -> number = 3
                R.id.imageButton_4 -> number = 4
                R.id.imageButton_5 -> number = 5
                R.id.imageButton_6 -> number = 6
                R.id.imageButton_7 -> number = 7
                R.id.imageButton_8 -> number = 8
                R.id.imageButton_9 -> number = 9
            }

            performAction(number, selectedImageButton)
        }
    }

    private fun clickResetGame() {
        mBtnReset?.setOnClickListener {
            winner = null
            activePlayer = ActivePlayers.CIRCLE
            binding.textViewCurrentPlayerTitle.text = getString(R.string.current_player)
            binding.textViewCurrentPlayer.text = getString(R.string.circle_o)

            playerOneSelections.clear()
            playerTwoSelections.clear()
            for (i in disabledImages.indices) {
                disabledImages[i]?.setImageResource(0)
                disabledImages[i]?.isEnabled = true
                disabledImages[i]?.setColorFilter(0)
            }
            disabledImages.clear()

            Toast.makeText(this, getString(R.string.reset_game), Toast.LENGTH_SHORT).show()
        }
    }

}