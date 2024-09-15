rootProject.name = "MDF"
sourceControl {
    gitRepository(java.net.URI.create("https://github.com/wrabot/competitive-tools.git")) {
        producesModule("wrabot.competitive:CompetitiveTools")
    }
}
include("generate")
