package cn.silenttt.tcs;

/**
 * Created by tt on 15-5-14.
 */
public class skill implements Runnable {
    public int skillNum=-1;
    public void setSkillNum(int x){
        this.skillNum=x;
    }
    @Override
    public void run() {
        while(true){
            switch (skillNum){
                case -1:break;
                case 0:{
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }skillNum=-1;

                }
                break;
                case 1:{
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    skillNum=-1;
                }
                break;
                case 2:{
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    skillNum=-1;
                }
                break;
            }
        }
    }
}
