package se.umu.milo0047

interface GameLogicProvider {
    var diceValues: List<Int>
    var diceHeld: List<Boolean>
    var rollsLeft: Int
    var rollButtonText: String
    var roundScore: Int
    var totalScore: Int
    var roundCount: Int
    var selectedScoringOption: String
    var usedScoringOptions: Set<String>
    var isScoringMenuOpen: Boolean
}