package computerPlayer;
public class ComputerPlayer {
    public boolean stop;
    private int difficultyMode;//0随机1贪心
    private void playMode0()
    {

    }
    private void playMode1()
    {

    }
    public  void ComputerPlayer(int diffMode)
    {
        difficultyMode=diffMode;
        Thread t = new Thread()
        {
            @Override
            public void run()
            {  // override the run() for the running behaviors
                while (true)
                {
                    if (stop) break;
                    if(difficultyMode==0)
                        playMode0();
                    else
                        playMode1();
                    try {sleep(50);}
                    catch (InterruptedException ex) {}
                }
            }
        };
    }
}
