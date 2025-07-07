type TeamInfo = {
  name: string,
  primary: string,
  secondary: string,
  logo?: string
}

const TeamInformation: Record<string, TeamInfo> = {
  SFG: { name: "San Francisco Giants",  primary: "#FD5A1E", secondary: "#000000", logo: "/src/public/sfgbaseball.webp" },
  BOS: { name: "Boston Red Sox",        primary: "#BD3039", secondary: "#192C55" },
  LAD: { name: "Los Angeles Dodgers",   primary: "#005A9C", secondary: "#EF3E42" },
  ATH: { name: "Oakland Athletics",     primary: "#003831", secondary: "#EFB21E" },
  TOR: { name: "Toronto Blue Jays",     primary: "#134A8E", secondary: "#E8291C" },
  MIL: { name: "Milwaukee Brewers",     primary: "#0A2351", secondary: "#B6922E" },
  PHI: { name: "Philadelphia Phillies", primary: "#E81828", secondary: "#002D72" },
  NYM: { name: "New York Mets",         primary: "#002D72", secondary: "#FF5910" },
  WSN: { name: "Washington Nationals",  primary: "#AB0003", secondary: "#212759" },
  NYY: { name: "New York Yankees",      primary: "#132448", secondary: "#C4CED3" },
  KCR: { name: "Kansas City Royals",    primary: "#174885", secondary: "#C0995A" },
  CHC: { name: "Chicago Cubs",          primary: "#0E3386", secondary: "#CC3433" },
  STL: { name: "St. Louis Cardinals",   primary: "#C41E3A", secondary: "#22205F" },
  ATL: { name: "Atlanta Braves",        primary: "#13274F", secondary: "#CE1141" },
  HOU: { name: "Houston Astros",        primary: "#002D62", secondary: "#EB6E1F" },
  SEA: { name: "Seattle Mariners",      primary: "#0C2C56", secondary: "#005C5C" },
  CIN: { name: "Cincinnati Reds",       primary: "#C6011F", secondary: "#000000" },
  ARI: { name: "Arizona Diamondbacks",  primary: "#A71930", secondary: "#E3D4AD" },
  SDP: { name: "San Diego Padres",      primary: "#002D62", secondary: "#C0995A" },
  DET: { name: "Detroit Tigers",        primary: "#182D55", secondary: "#F26722" },
  LAA: { name: "Los Angeles Angels",    primary: "#BA0021", secondary: "#003263" },
  TBR: { name: "Tampa Bay Rays",        primary: "#092C5C", secondary: "#8FBCE6" },
  PIT: { name: "Pittsburgh Pirates",    primary: "#000000", secondary: "#FDB827" },
  CLE: { name: "Cleveland Guardians",   primary: "#00385D", secondary: "#E50022" },
  TEX: { name: "Texas Rangers",         primary: "#003278", secondary: "#C0111F" },
  CHW: { name: "Chicago White Sox",     primary: "#000000", secondary: "#C4CED4" },
  COL: { name: "Colorado Rockies",      primary: "#33006F", secondary: "#C4CED4" },
  MIN: { name: "Minnesota Twins",       primary: "#002B5C", secondary: "#D31145" },
  BAL: { name: "Baltimore Orioles",     primary: "#DF4601", secondary: "#000000" },
  MIA: { name: "Miami Marlins",         primary: "#000000", secondary: "#FF6600" }
}

export default TeamInformation