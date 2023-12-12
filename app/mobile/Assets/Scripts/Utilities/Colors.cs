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
        
        public Color skyBlueLight;
        public Color blueGreenLight;
        public Color prussianBlueLight;
        public Color selectiveYellowLight;
        public Color UTOrangeLight;
        public Color ChineseVioletLight;
        public Color CeladonLight;
        public Color VanillaLight;

        public Color skyBlueDark;
        public Color blueGreenDark;
        public Color prussianBlueDark;
        public Color selectiveYellowDark;
        public Color UTOrangeDark;
        public Color ChineseVioletDark;
        public Color CeladonDark;
        public Color VanillaDark;

        public Colors()
        {
            initializeColor(out skyBlueLight, "#CDE8F4");
            initializeColor(out skyBlue, "#8ECAE6");
            initializeColor(out skyBlueDark, "#68BE96");
            
            initializeColor(out blueGreenLight, "#40BEDD");
            initializeColor(out blueGreen, "#219EBC");
            initializeColor(out blueGreenDark, "#18748B");
            
            initializeColor(out prussianBlueLight, "#045E8B");
            initializeColor(out prussianBlue, "#023047");
            initializeColor(out prussianBlueDark, "#010D14");
            
            initializeColor(out selectiveYellowLight, "#FFCB47");
            initializeColor(out selectiveYellow, "#FFB703");
            initializeColor(out selectiveYellowDark, "#CC9200");
            
            initializeColor(out UTOrangeLight, "#FFA947");
            initializeColor(out UTOrange, "#FB8500");
            initializeColor(out UTOrangeDark, "#CC6D00");
            
            initializeColor(out ChineseVioletLight, "#A4798D");
            initializeColor(out ChineseViolet, "#845A6D");
            initializeColor(out ChineseVioletDark, "#614250");
            
            initializeColor(out CeladonLight, "#DEF7EC");
            initializeColor(out Celadon, "#A1E8AF");
            initializeColor(out CeladonDark, "#7CDF90");
            
            initializeColor(out VanillaLight, "#FBF8DA");
            initializeColor(out Vanilla, "#F5EE9E");
            initializeColor(out VanillaDark, "#EFE46B");
        }

        private void initializeColor(out Color color, string hex)
        {
            UnityEngine.ColorUtility.TryParseHtmlString( hex, out color);
        }
        
        
    }
}