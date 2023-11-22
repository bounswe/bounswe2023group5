using UnityEngine;

namespace Utilities
{
    public class Colors
    {
        private string[] colorList = new[]
        {
            "#8ECAE6", "#219EBC", "#023047", "#FFB703", "#FB8500",
            "#845A6D", "#A1E8AF", "#F5EE9E"
        };

        public Color skyBlue;
        public Color blueGreen;
        public Color prussianBlue;
        public Color selectiveYellow;
        public Color UTOrange;
        public Color ChineseViolet;
        public Color Celadon;
        public Color Vanilla;


        public Colors()
        {
            initializeColor(out skyBlue, 0);
            initializeColor(out blueGreen, 1);
            initializeColor(out prussianBlue, 2);
            initializeColor(out selectiveYellow, 3);
            initializeColor(out UTOrange, 4);
            initializeColor(out ChineseViolet, 5);
            initializeColor(out Celadon, 6);
            initializeColor(out Vanilla, 7);
        }

        private void initializeColor(out Color color, int order)
        {
            UnityEngine.ColorUtility.TryParseHtmlString( colorList[order],
                out color);
        }
        
        
    }
}