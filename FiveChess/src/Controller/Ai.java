package Controller;


import java.util.HashMap;

import utils.BoardUtil;

public class Ai {

    HashMap<String, Integer> hm = null;
    HashMap<String, Integer> hm2 = null;
    private int x;
    private int y;
    private int score;          //该处落点得分
    private  int[][] position;  //位置评分二维数组
    private  String[] note;     //评分标记数组
    private  String[] protect;  //防守参照数组

    public Ai() {

        x = 0;
        y = 0;
        score = 0;
        hm = new HashMap<>();
        hm2 = new HashMap<>();
        position = new int[][]{          //根据棋盘分布问题，越中心的点分值应当越高
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
                { 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
                { 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 },
                { 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
                { 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 },
                { 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
                { 0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0 },
                { 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
                { 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 },
                { 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
                { 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 },
                { 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

        ClearStringArray();     //初始化数组

    }

    //重新开始
    public void ReStart() {
        ClearStringArray();
        this.hm.clear();
        this.hm2.clear();
        HMSet();
    }

    //判断棋子周围是否有棋子，没有则不下该处
    private boolean CheckArround(int x, int y) {
        int i,j,minx = 0, miny = 0, maxx = 14, maxy = 14;

        if(x > 0)
            minx = x - 1;
        if(y > 0)
            miny = y - 1;
        if(x < 14)
            maxx = x + 1;
        if(y < 14)
            maxy = y + 1;

        for(i = minx; i <= maxx; i++) {
            for(j = miny; j <= maxy; j++) {
                if(i != x || j != y) {
                    if(BoardUtil.chess[i][j] != '0')
                        return true;
                }
            }
        }
        return false;
    }

    /*
     * 找出电脑的最优下法
     */
    public void JudgeBest() {

        HMSet();	//索引评分表
        TryBest();		//根据评分表综合分析最佳下法

    }

    private void TryBest() {

        int i, j, MAX_1, MAX_2, random;
        char Ai, Play;
        this.score = 0;         //每次下棋尝试前 score 值为 0
        if(BoardUtil.isAIFirst()) {
            Ai = 'B';
            Play = 'W';
        } else {
            Ai = 'W';
            Play = 'B';
        }

        for ( i = 0; i < 15; i++) {
            for ( j = 0; j < 15; j++) {
                if (BoardUtil.chess[i][j] == '0' && CheckArround(i, j)) {	//此处为空，可落子



                    //  人机尝试下该处，获得得分
                    MAX_1 = TryPlay(Ai, i, j, note, hm);
                    //  人机尝试帮玩家下该处，判断是否要防守，阻止玩家获胜
                    MAX_2 = TryPlay(Play, i ,j, protect, hm2);  // 测试玩家下一步是否有活三或活四或长连的可能
                    //  最后更新值
                    random = (int)(Math.random()*2);
                    if(MAX_1 > MAX_2) {
                        if(MAX_1 > score) {
                            score = MAX_1;
                            this.x = i;
                            this.y = j;
                        } else if(MAX_1 == score) {
                            if(random == 1) {                   //设置一定的随机性
                                this.x = i;
                                this.y = j;
                            }
                        }
                    } else {
                        if(MAX_2 > score) {
                            score = MAX_2;
                            this.x = i;
                            this.y = j;
                        } else if(MAX_2 == score) {
                            if(random == 1) {                   //设置一定的随机性
                                this.x = i;
                                this.y = j;
                            }
                        }
                    }

                }
            }
        }

    }


    //录入评分表
    private void HMSet() {

        // 人机防守
        hm2.put( protect[0] , 20000);
        hm2.put( protect[1] , 6000);
        hm2.put( protect[2] , 150);
        hm2.put( protect[3] , 700);
        hm2.put( protect[4] , 700);
        hm2.put( protect[5] , 100);
        hm2.put( protect[6] , 100);
        // 人机进攻

        // 长连
        hm.put( note[0] , 100000);
        // 活四
        hm.put( note[1] , 10000);
        // 冲四
        hm.put( note[2] , 500);
        hm.put( note[3] , 500);
        hm.put( note[4] , 500);
        hm.put( note[5] , 500);
        hm.put( note[6] , 500);
        // 活三
        hm.put( note[7] , 200);
        hm.put( note[8] , 200);
        hm.put( note[9] , 200);
        // 眠三
        hm.put( note[10] , 50);
        hm.put( note[11] , 50);
        hm.put( note[12] , 50);
        hm.put( note[13] , 50);
        hm.put( note[14] , 50);
        // 活二
        hm.put( note[15] , 5);
        hm.put( note[16] , 5);
        hm.put( note[17] , 5);
        // 眠二
        hm.put( note[18] , 3);
        hm.put( note[19] , 3);
        hm.put( note[20] , 3);
        hm.put( note[21] , 3);
        hm.put( note[22] , 3);
        hm.put( note[23] , 3);
        // 死四
        hm.put( note[24] , -5);
        // 死三
        hm.put( note[25] , -5);
        hm.put( note[26] , -5);
        hm.put( note[27] , -5);
        // 死二
        hm.put( note[28] , -5);
        hm.put( note[29] , -5);
        hm.put( note[30] , -5);

    }

    //尝试下该处的得分情况
    private int TryPlay(char me, int i, int j, String Note[], HashMap<String, Integer> Hm ) {
        String model = "";
        int MAX_1 = 0, score_1 = 0, score_2 = 0, score_3 = 0, score_4 = 0;
        int m, n, Bg4 = 0, Ag3 = 0, Bg3 = 0, Ag2 = 0, Bg2 = 0;  //冲四，活三。眠三，活二，眠二个数均置 0
        //人机尝试下该处
        BoardUtil.chess[i][j] = me;
        //  横向扫描
        for( m = 0; m < 15; m++) {
            model += BoardUtil.chess[i][m];
        }
        for(n = 0; n < Note.length; n++) {
            if(model.contains(Note[n])) {           //如果包含, 则找到一种情况
                if(Hm.get(Note[n]) > score_1) {         //更新分值
                    score_1 = Hm.get(Note[n]);
                    if(score_1 > MAX_1) {
                        MAX_1 = score_1;
                    }
                }
            }

        }
        if(score_1 == 500 || score_1 == 700) {
            Bg4++;
        } else if(score_1 == 200 || score_1 == 150) {
            Ag3++;
        } else if(score_1 == 50) {
            Bg3++;
        } else if(score_1 == 5) {
            Ag2++;
        } else if(score_1 == 3) {
            Bg2++;
        }

        model = "";  //置空进行下一次扫描
        //  纵向扫描
        for( m = 0; m < 15; m++) {
            model += BoardUtil.chess[m][j];
        }
        for(n = 0; n < Note.length; n++) {
            if(model.contains(Note[n])) {           //如果包含, 则找到一种情况
                if(Hm.get(Note[n]) > score_2) {       //更新分值
                    score_2 = Hm.get(Note[n]);
                    if(score_2 > MAX_1) {
                        MAX_1 = score_2;
                    }
                }
            }
        }
        if(score_2 == 500 || score_2 == 700) {
            Bg4++;
        } else if(score_2 == 200 || score_2 == 150) {
            Ag3++;
        } else if(score_2 == 50) {
            Bg3++;
        } else if(score_2 == 5) {
            Ag2++;
        } else if(score_2 == 3) {
            Bg2++;
        }

        model = "";  //置空进行下一次扫描
        //  右斜向扫描
        if((i+j) > 14) {
            m = 14;
            n = i + j - 14;
        } else {
            m = i + j;
            n = 0;
        }
        for(;m >= 0 && n >= 0 && m < 15 && n < 15; m--, n++) {
            model += BoardUtil.chess[m][n];
        }
        for(n = 0; n < Note.length; n++) {
            if(model.contains(Note[n])) {           //如果包含, 则找到一种情况
                if(Hm.get(Note[n]) > score_3) {       //更新分值
                    score_3 = Hm.get(Note[n]);
                    if(score_3 > MAX_1) {
                        MAX_1 = score_3;
                    }
                }
            }
        }
        if(score_3 == 500 || score_3 == 700) {
            Bg4++;
        } else if(score_3 == 200 || score_3 == 150) {
            Ag3++;
        } else if(score_3 == 50) {
            Bg3++;
        } else if(score_3 == 5) {
            Ag2++;
        } else if(score_3 == 3) {
            Bg2++;
        }

        model = "";  //置空进行下一次扫描
        //  左斜向扫描
        if(i > j) {
            m = i - j;
            n = 0;
        } else {
            m = 0;
            n = j - i;
        }
        for(;m >= 0 && n >= 0 && m < 15 && n < 15; m++, n++) {
            model += BoardUtil.chess[m][n];
        }
        for(n = 0; n < Note.length; n++) {
            if(model.contains(Note[n])) {           //如果包含, 则找到一种情况
                if(Hm.get(Note[n]) > score_4) {       //更新分值
                    score_4 = Hm.get(Note[n]);
                    if(score_4 > MAX_1) {
                        MAX_1 = score_4;
                    }
                }
            }
        }
        if(score_4 == 500 || score_4 == 700) {
            Bg4++;
        } else if(score_4 == 200 || score_4 == 150) {
            Ag3++;
        } else if(score_4 == 50) {
            Bg3++;
        } else if(score_4 == 5) {
            Ag2++;
        } else if(score_4 == 3) {
            Bg2++;
        }
        char myself;
        if(BoardUtil.isAIFirst()) {
            myself = 'B';
        } else {
            myself = 'W';
        }

        if(Bg4 >= 2 || (Bg4 == 1 && Ag3 == 1) && myself == me) {    //双冲四，冲四活三
            MAX_1 = 10000;
        } else if(Bg4 >= 2 || (Bg4 == 1 && Ag3 == 1) && myself != me) {
            MAX_1 = 9000;
        } else if(Ag3 >= 2 && myself == me) {                       //双活三
            MAX_1 = 5000;
        } else if(Ag3 >= 2 && myself != me) {
            MAX_1 = 3000;
        } else if(Ag3 == 1 && Bg3 == 1 && myself == me) {           //活三眠三
            MAX_1 = 1000;
        } else if(Ag3 == 1 && Bg3 == 1 && myself != me) {
            MAX_1 = 300;
        } else if(Ag2 == 1 && Bg2 == 1 && myself == me) {			//活二眠二
            MAX_1 = 30;
        } else if(Ag2 == 1 && Bg2 == 1 && myself != me) {
            MAX_1 = 10;
        }

        BoardUtil.chess[i][j] = '0';                //尝试完毕，还原
        return MAX_1 + position[i][j];              //位置加分

    }
    //重新初始化数组
    private void ClearStringArray() {
        if(BoardUtil.isAIFirst()) {
            note = new String[]{        //参照表
                    "BBBBB", "0BBBB0", "0BBBBW", "WBBBB0", "B0BBB", "BB0BB", "BBB0B",
                    "0BBB0", "0B0BB0", "0BB0B0", "00BBBW", "0B0BBW", "0BB0BW", "B00BB",
                    "BOBOB", "00BB0", "0B0B0", "0B00B0", "000BBW", "00B0BW", "0B00BW",
                    "B000B", "W0B0B0W", "W0BB00W", "WBBBBW", "WBBBW", "W0BBBW", "WBBB0W",
                    "WBBW", "W0BBW", "WBB0W"
            };
            protect = new String[]{     //防守参照表
                    "WWWWW", "0WWWW0", "0WWW0", "0W0WW0", "0WW0W0", "0WWWWB", "BWWWW0"
            };

        } else {
            note = new String[]{
                    "WWWWW", "0WWWW0", "0WWWWB", "BWWWW0", "WOWWW", "WWOWW", "WWWOW",
                    "OWWWO", "OWOWWO", "OWWOWO", "OOWWWB", "0W0WWB", "0WW0WB", "W00WW",
                    "W0W0W", "00WW0", "0W0W0", "0W00W0", "000WWB", "00W0WB", "0W00WB",
                    "W000W", "B0W0W0B", "B0WW00B", "BWWWWB", "BWWWB", "B0WWWB", "BWWW0B",
                    "BWWB", "B0WWB", "BWW0B"
            };
            protect = new String[]{
                    "BBBBB", "0BBBB0", "0BBB0", "0B0BB0", "0BB0B0", "0BBBBW", "WBBBB0"
            };
        }
    }

    public int getX() {		return x;	 }
    public void setX(int x) { 	this.x = x;	 }
    public int getY() {		return y;	 }
    public void setY(int y) {	this.y = y;		}
}
