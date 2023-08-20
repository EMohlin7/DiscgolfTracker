package se.umu.edmo0011.discgolftracker

// Used for saving and retrieving data for MatchViewModel when it is destroyed before the match is
// finished. This can happen when navigating to another screen using the navbar during a match.
class OngoingMatch {
    companion object{
        private var instance: OngoingMatch? = null
        fun getInstance(): OngoingMatch
        {
            if (instance == null)
                instance = OngoingMatch()

            return instance!!
        }
    }
    private constructor()

    var holes: List<Hole>? = null; private set
    var startTime: Long? = null; private set

    fun save(holes: List<Hole>, startTime: Long)
    {
        this.holes = holes
        this.startTime = startTime
    }
    fun clearMatch()
    {
        holes = null
        startTime = null
    }
}